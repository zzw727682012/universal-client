package com.universal.client.server;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
public class HttpRequest {
    private static Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;
    private String version;
    private String requestUrl;
    private String absoluteUrl;
    private String method;
    private String host;
    private String user_agent;
    private String range;
    private String content_type;
    private String body;
    private Map<String, String> cookies;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public static HttpRequest parseRequest(String texts) {
        final HttpRequest request = new HttpRequest();
        List<String> textList = Arrays.stream(texts.split("\r\n")).collect(Collectors.toList());
        Map<String, String> maps = textList.stream().flatMap(x -> {
            if (textList.indexOf(x) == 0) {
                String[] strings = x.trim().split(" ");
                return Stream.of("method:" + strings[0].trim(), "url:" + strings[1].trim(), "http:" + strings[2].trim());
            } else if (textList.lastIndexOf(x) == textList.size() - 1) {
                //最后一行为body
                return Stream.of("body:" + x.trim());
            } else {
                //请求头
                return Stream.of(x.toLowerCase().replace("-", "_").replace(" ", "").trim());
            }
        }).filter(x -> x.matches("(.+):(.+)")).collect(Collectors.toMap(x -> x.split(":")[0], x -> x.split(":")[1], (x, y) -> x));

        String url = maps.get("url"); //   /aaa/bbb?test=123
        String requestUrl = url.indexOf("?") > 0 ? url.substring(0, url.indexOf("?")) : url;

        request.setRequestUrl(requestUrl);
        request.setAbsoluteUrl(url);
        request.setParameters(parameters(url));
        request.setHeaders(maps);

        Arrays.stream(HttpRequest.class.getDeclaredFields()).forEach(field -> {
            if (maps.containsKey(field.getName())) {
                field.setAccessible(true);
                try {
                    field.set(request, maps.get(field.getName()));
                } catch (IllegalAccessException e) {
                    logger.error("set filed:{} value error",field.getName(), e);
                }
            }
        });

        try {
            for (Field field : HttpRequest.class.getDeclaredFields()) {
                if (!maps.containsKey(field.getName())) continue;
                field.setAccessible(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public static void main(String[] args) {
        String URL="/aaa/bbb/ccc/ddd?test=123";
        String URLS = URL.indexOf("?") > 0 ? URL.substring(0, URL.indexOf("?")) : URL;
        System.out.println(URLS);
        System.out.println(URL.charAt(0));
        System.out.println(URL.charAt(1));
    }

    private static Map<String, String> parameters(String urls) {
        Map<String, String> maps = new HashMap<String, String>();
        try {
            if (urls == null) return maps;
            boolean isurls = false, isstop = true;
            String parameter = ((isurls = urls.contains("?")) ? urls.substring(urls.indexOf("?") + 1) : (isstop = urls.indexOf("/", 2) > 0) ? urls.substring(urls.indexOf("/", 2) + 1) : urls).replace(" ", "");
            parameter = URLDecoder.decode(parameter, "UTF-8");
            if (!isstop) return maps;
            String[] paras = parameter.split(isurls ? "&" : "/");
            return IntStream.range(0, paras.length).mapToObj(x -> {
                if (!paras[x].contains("=")) return String.format("%s=%s", x, paras[x]);
                return paras[x];
            }).filter(x -> x.matches("(.+)=(.+)")).collect(Collectors.toMap(x -> x.split("=")[0], x -> x.split("=")[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }
    public Map<String, String> GetParms(String... name) {
        if (parameters == null) return null;
        final int maxvalue = (int) parameters.keySet().stream().filter(x -> x.matches("\\d+")).count();
        if (name.length < 2) {
            final StringJoiner builders = new StringJoiner("/");
            IntStream.range(0, (int) maxvalue).forEach(x -> builders.add(parameters.get(String.valueOf(x))));
            return null;//ImmutableMap.of(name[0], builders.toString());
        }
        return IntStream.range(0, Math.min(name.length, maxvalue)).boxed().collect(Collectors.toMap(x -> name[x], x -> parameters.get(String.valueOf(x)), (x, y) -> x));
    }

    public String GetParm(String name) {
        if (parameters == null) return null;
        return parameters.get(name);
    }

    public <T> T GetParmToBean(Class<T> beans, String... parms) {
        try {
            Map<String, String> paras = (parms.length > 0) ? GetParms(parms) : parameters;
            if (paras == null) return null;
            System.out.println("..................................");
            if (Modifier.isAbstract(beans.getModifiers()) || Modifier.isInterface(beans.getModifiers()) || !Stream.of(beans.getConstructors()).anyMatch(x -> x.getParameterCount() < 1))
                return null;
            T objs = beans.newInstance();
            for (Field fields : beans.getDeclaredFields()) {
                String names = fields.getName();
                Class<?> types = fields.getType();
                if (!paras.containsKey(names)) continue;
                fields.setAccessible(true);
                if (Number.class.isAssignableFrom(types)) if (!paras.get(names).matches("\\d+")) continue;
                if (types.isEnum()) {
                    @SuppressWarnings({"unchecked", "rawtypes"}) Enum<?> enums = Enum.valueOf((Class<Enum>) types, paras.get(names).toUpperCase());
                    fields.set(objs, enums);
                    continue;
                }
                Constructor<?> constructors = Stream.of(types.getConstructors()).filter(x -> {
                    Class<?>[] classs = x.getParameterTypes();
                    return classs.length == 1 && classs[0] == String.class;
                }).findFirst().orElse(null);
                if (constructors != null) fields.set(objs, constructors.newInstance(paras.get(names)));
            }
            return objs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
