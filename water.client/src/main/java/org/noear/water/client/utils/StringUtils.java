package org.noear.water.client.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class StringUtils {
    static final String[] padding = new String[]{"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    "};
    private static final Stack<StringBuilder> builders = new Stack();
    private static final int MaxCachedBuilderSize = 8192;
    private static final int MaxIdleBuilders = 8;

    private StringUtils() {
    }

    public static String join(Collection strings, String sep) {
        return join(strings.iterator(), sep);
    }

    public static String join(Iterator strings, String sep) {
        if (!strings.hasNext()) {
            return "";
        } else {
            String start = strings.next().toString();
            if (!strings.hasNext()) {
                return start;
            } else {
                StringBuilder sb = borrowBuilder().append(start);

                while(strings.hasNext()) {
                    sb.append(sep);
                    sb.append(strings.next());
                }

                return releaseBuilder(sb);
            }
        }
    }

    public static String join(String[] strings, String sep) {
        return join((Collection) Arrays.asList(strings), sep);
    }

    public static String padding(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("width must be > 0");
        } else if (width < padding.length) {
            return padding[width];
        } else {
            char[] out = new char[width];

            for(int i = 0; i < width; ++i) {
                out[i] = ' ';
            }

            return String.valueOf(out);
        }
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isBlank(String string) {
        if (string != null && string.length() != 0) {
            int l = string.length();

            for(int i = 0; i < l; ++i) {
                if (!isWhitespace(string.codePointAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNumeric(String string) {
        if (string != null && string.length() != 0) {
            int l = string.length();

            for(int i = 0; i < l; ++i) {
                if (!Character.isDigit(string.codePointAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isWhitespace(int c) {
        return c == 32 || c == 9 || c == 10 || c == 12 || c == 13;
    }

    public static boolean isActuallyWhitespace(int c) {
        return c == 32 || c == 9 || c == 10 || c == 12 || c == 13 || c == 160;
    }

    public static boolean isInvisibleChar(int c) {
        return Character.getType(c) == 16 && (c == 8203 || c == 8204 || c == 8205 || c == 173);
    }

    public static String normaliseWhitespace(String string) {
        StringBuilder sb = borrowBuilder();
        appendNormalisedWhitespace(sb, string, false);
        return releaseBuilder(sb);
    }

    public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;
        int len = string.length();

        int c;
        for(int i = 0; i < len; i += Character.charCount(c)) {
            c = string.codePointAt(i);
            if (isActuallyWhitespace(c)) {
                if ((!stripLeading || reachedNonWhite) && !lastWasWhite) {
                    accum.append(' ');
                    lastWasWhite = true;
                }
            } else if (!isInvisibleChar(c)) {
                accum.appendCodePoint(c);
                lastWasWhite = false;
                reachedNonWhite = true;
            }
        }

    }

    public static boolean in(String needle, String... haystack) {
        int len = haystack.length;

        for(int i = 0; i < len; ++i) {
            if (haystack[i].equals(needle)) {
                return true;
            }
        }

        return false;
    }

    public static boolean inSorted(String needle, String[] haystack) {
        return Arrays.binarySearch(haystack, needle) >= 0;
    }

    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        if (relUrl.startsWith("?")) {
            relUrl = base.getPath() + relUrl;
        }

        if (relUrl.indexOf(46) == 0 && base.getFile().indexOf(47) != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }

        return new URL(base, relUrl);
    }

    public static String resolve(String baseUrl, String relUrl) {
        try {
            URL base;
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException var5) {
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }

            return resolve(base, relUrl).toExternalForm();
        } catch (MalformedURLException var6) {
            return "";
        }
    }

    //借用StringBuilder（基于Stack管理）
    public static StringBuilder borrowBuilder() {
        synchronized(builders) {
            return builders.empty() ? new StringBuilder(MaxCachedBuilderSize) : builders.pop();
        }
    }

    //释放StringBuilder（基于Stack管理）
    public static String releaseBuilder(StringBuilder sb) {
        AssertUtils.notNull(sb);
        String string = sb.toString();
        if (sb.length() > MaxCachedBuilderSize) {
            sb = new StringBuilder(MaxCachedBuilderSize);
        } else {
            sb.delete(0, sb.length());
        }

        synchronized(builders) {
            builders.push(sb);

            while(builders.size() > MaxIdleBuilders) {
                builders.pop();
            }

            return string;
        }
    }
}
