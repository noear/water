package webapp.dao;

import webapp.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitUtil {

    public static List<String> tags(String project) {

        ShellResult ret = ShellUtil.exec(Config.ops_host, "project=" + project + ";", ShellText.GIT_TAG);

        if (ret.isOk()) {
            return Arrays.asList(ret.getOutput().split("\n"));
        }
        return new ArrayList<>();
    }
}
