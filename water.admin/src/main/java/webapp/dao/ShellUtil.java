package webapp.dao;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.noear.water.tools.TextUtils;
import webapp.Config;
import webapp.dao.db.DbWindApi;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Shell命令执行工具
 *
 * @author lht
 * @date 2018/12/07
 */
public class ShellUtil {

    private static final String PRIVATE_KEY_PATH = Config.private_key_path;
    private static final String DEFAULT_USER = "root";

    private static final Pattern FUN_PATTERN = Pattern.compile("\\@\\[\\w+\\(([\\w]+)=([\\w/.]+)(\\s*,\\s*([\\w]+)=([\\w/.]+))*\\)\\]");

    public static ShellResult exec(EnvModel env, String cmd) {

        return exec(env, "", cmd);
    }

    public static ShellResult exec(EnvModel env, String args, String cmd) {

        List<String> targets = env.targets;

        StringBuilder e = new StringBuilder();
        env.forEach((k, v) -> e.append(k).append("=").append(v).append(";"));
        e.append(args);

        boolean isOk = true;
        StringBuilder sb = new StringBuilder();
        sb.append(targets.toString()).append("\n\n");
        for (String target: targets) {
            ShellResult result = exec(target, DEFAULT_USER, e.toString(), cmd);
            sb.append(target).append("\n").append(result.output);
            if (!result.isOk) {
                isOk = false;
                break;
            }
        }

        return new ShellResult(isOk, sb.toString());
    }

    public static ShellResult exec(String hostname, String cmd) {
        return exec(hostname, DEFAULT_USER, "", cmd);
    }

    public static ShellResult exec(String hostname, String args, String cmd) {
        return exec(hostname, DEFAULT_USER, args, cmd);
    }

    private static ShellResult exec(String hostname, String username, String args, String cmd) {

        Session session = null;
        try {
            session = getSession(hostname, username, PRIVATE_KEY_PATH);

            cmd = args + "\n" + compile(cmd);

            return exec(session, cmd);
        } catch (Exception e) {
            return new ShellResult(false);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private static String compile(String code) throws SQLException {

        Matcher m = FUN_PATTERN.matcher(code);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String mix = m.group();
            int start = mix.indexOf("(");
            int end = mix.indexOf(")");
            String fun = mix.substring(2, start);

            String funBody = DbWindApi.getScriptByName(fun).code;

            String params = mix.substring(start + 1, end);
            String localParams = Arrays.stream(params.split(","))
                                       .map(s -> "local " + s)
                                       .collect(Collectors.joining(";"));

            sb.append(fun).append("(){\n")
              .append(localParams).append("\n")
              .append(funBody).append("\n}\n");

            code = code.replace(mix, fun);
        }
        return sb.toString() + code;
    }

    private static Session getSession(String hostname, String username, String privateKeyPath) {

        Session session = null;
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(TextUtils.isEmpty(username) ?  DEFAULT_USER : username, hostname);

            if (session == null) {
                return null;
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(30000);
        } catch (Exception e) {
            try {
                if (session.isConnected()) {
                    session.disconnect();
                }
            } catch (Exception ex) {

            }
        }
        return session;
    }

    private static ShellResult exec(Session session, String cmd) throws Exception {

        if (session == null) {
            return new ShellResult(false);
        }

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        InputStream in = channelExec.getInputStream();
        channelExec.connect(30000);
        int res;
        StringBuffer buf = new StringBuffer(1024);
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                buf.append(new String(tmp, 0, i));
            }
            if (channelExec.isClosed()) {
                res = channelExec.getExitStatus();
                break;
            }
            TimeUnit.MILLISECONDS.sleep(200);
        }
        channelExec.disconnect();
        return new ShellResult(res == 0, buf.toString());
    }
}
