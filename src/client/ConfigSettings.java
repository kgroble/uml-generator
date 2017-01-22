package client;

import graph.AccessLevel;
import graph.ClassCell;
import patterns.Pattern;
import patterns.PatternDecorator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by lewis on 1/20/17.
 */
public class ConfigSettings {
    private static final String SETTINGS_FLAG = "--settings=";
    private static final String DEFAULT_SETTINGS_FILE = "/home/lewis/curr-programs/uml-generator/config/settings.txt";
    private static final String RECURSIVE_TAG = "recursive";
    private static final String SYNTHETIC_TAG = "synthetic";
    private static final String WHITELIST_TAG = "include";
    private static final String BLACKLIST_TAG = "exclude";
    private static final String PATTERNS_TAG = "patterns";
    private static final String ACCESS_TAG = "access";

    private static boolean isRecursive = false;
    private static boolean showSynthetic = true;
    private static List<String> whiteList = new ArrayList<>();
    private static List<String> blackList = new ArrayList<>();
    private static List<Pattern> patterns = new ArrayList<>();
    private static AccessLevel accessLevel = AccessLevel.PRIVATE;
    private static Properties properties;

    public static boolean getRecursive() {
        return isRecursive;
    }

    public static boolean getShowSynthetic() {
        return showSynthetic;
    }

    public static List<String> getWhiteList() {
        return whiteList;
    }

    public static List<String> getBlackList() {
        return blackList;
    }

    public static List<Pattern> getPatterns() {
        return patterns;
    }

    public static AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public static String getAssociatedVal(String key) {
        return properties.getProperty(key);
    }

    public static boolean classInBlacklist(String className) {
        for (String listedItem : blackList) {
            if (className.contains(listedItem)) {
                return true;
            }
        }

        return false;
    }

    public static void setupConfig(String[] args) throws IOException {
        for (String arg : args) {
            if (arg.contains(SETTINGS_FLAG)) {
                properties = new Properties();
                properties.load(new FileInputStream(arg.substring(SETTINGS_FLAG.length())));
                break;
            }
        }

        if (properties == null) {
            properties = new Properties();
            properties.load(new FileInputStream(DEFAULT_SETTINGS_FILE));
        }

        String buff;
        buff = properties.getProperty(RECURSIVE_TAG, "");
        if (buff.equals("true")) {
            isRecursive = true;
        } else if (buff.equals("false")) {
            isRecursive = false;
        }

        buff = properties.getProperty(SYNTHETIC_TAG, "");
        if (buff.equals("true")) {
            showSynthetic = true;
        } else if (buff.equals("false")) {
            showSynthetic = false;
        }

        buff = properties.getProperty(WHITELIST_TAG, "");
        if (!buff.equals("")) {
            for (String className : buff.split(" ")) {
                whiteList.add(className);
            }
        }

        buff = properties.getProperty(BLACKLIST_TAG, "");
        if (!buff.equals("")) {
            for (String packPref : buff.split(" ")) {
                blackList.add(packPref);
            }
        }

        buff = properties.getProperty(PATTERNS_TAG, "");
        if (!buff.equals("")) {
            String[] patChains = buff.split(";");
            for (int j = 0; j < patChains.length; j++) {
                String[] classNames = patChains[j].trim().split(" ");
                Pattern patt;
                PatternDecorator dec;
                if (classNames.length == 0) {
                    continue;
                }

                try {
                    patt = (Pattern) Class.forName(classNames[0]).newInstance();


                    for (int i = 1; i < classNames.length; i++) {
                        dec = (PatternDecorator) Class.forName(classNames[i]).newInstance();
                        dec.setInner(patt);

                        patt = dec;
                    }

                    patterns.add(patt);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        buff = properties.getProperty(ACCESS_TAG, "");
        if (buff.equals("public")) {
            accessLevel = AccessLevel.PUBLIC;
        } else if (buff.equals("private")) {
            accessLevel = AccessLevel.PRIVATE;
        } else if (buff.equals("protected")) {
            accessLevel = AccessLevel.PROTECTED;
        }

        for (String arg : args) {
            switch(arg) {
            case "-r":
            case "--recursive":
                isRecursive = true;
                break;
            case "--public":
                accessLevel = AccessLevel.PUBLIC;
                break;
            case "--private":
                accessLevel = AccessLevel.PRIVATE;
                break;
            case "--protected":
                accessLevel = AccessLevel.PROTECTED;
            default:
                whiteList.add(arg);
                break;
            }
        }
    }
}
