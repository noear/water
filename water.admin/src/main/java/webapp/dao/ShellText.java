package webapp.dao;

/**
 * @author dhb
 * @date 2018/12/07
 */
public class ShellText {

    public static final String RESTART_SERVICE = "sed -i \"/127.0.0.1:${port}/s/^.*$/  server 127.0.0.1:${port} down;/g\"  /data/_nginx.conf/${domain}\n" +
            "nginx -s reload\n" +
            "\n" +
            "sleep 3\n" +
            "\n" +
            "systemctl restart ${project}\n" +
            "\n" +
            "while :\n" +
            "    do\n" +
            "        curl -m 3 -sI  \"http://127.0.0.1:${port}/run/check/\" | grep 200\n" +
            "        result=$?\n" +
            "        if [[ $result -eq 0 ]]\n" +
            "            then\n" +
            "            echo \"service is ok\"\n" +
            "            break\n" +
            "        fi\n" +
            "        sleep 3\n" +
            "    done\n" +
            "\n" +
            "sed -i \"/127.0.0.1:${port}/s/^.*$/    server 127.0.0.1:${port} weight=10;/g\"  /data/_nginx.conf/${domain}\n" +
            "nginx -s reload";

    public static final String GIT_TAG = "cd /data/git/${project}\n" +
            "git fetch -a\n" +
            "git tag\n";
}
