package org.noear.liquor;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 动态编译器（线程不安全）
 *
 * This code mainly from: Arthas project
 * */
public class DynamicCompiler {
    private final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    private final StandardJavaFileManager standardFileManager;
    private final List<String> options = new ArrayList<String>();
    private final ClassLoader parentClassLoader;

    private final Collection<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
    private final List<Diagnostic<? extends JavaFileObject>> errors = new ArrayList<Diagnostic<? extends JavaFileObject>>();
    private final List<Diagnostic<? extends JavaFileObject>> warnings = new ArrayList<Diagnostic<? extends JavaFileObject>>();

    private DynamicClassLoader dynamicClassLoader;

    public DynamicCompiler() {
        this(null);
    }

    public DynamicCompiler(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        if (javaCompiler == null) {
            throw new IllegalStateException(
                    "Can not load JavaCompiler from javax.tools.ToolProvider#getSystemJavaCompiler(),"
                            + " please confirm the application running in JDK not JRE.");
        }
        standardFileManager = javaCompiler.getStandardFileManager(null, null, null);

        options.add("-Xlint:unchecked");
        options.add("-g");
        options.add("-XDuseUnsharedTable");//可避免 SharedNameTable 内存大涨

        parentClassLoader = classLoader;
    }

    /**
     * 获取类加载器
     */
    public DynamicClassLoader getClassLoader() {
        if (dynamicClassLoader == null) {
            dynamicClassLoader = new DynamicClassLoader(parentClassLoader);
        }

        return dynamicClassLoader;
    }

    /**
     * 获取选项
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * 切换类加载器
     *
     * @param dynamicClassLoader 动态类加载器
     */
    public void setClassLoader(DynamicClassLoader dynamicClassLoader) {
        this.dynamicClassLoader = dynamicClassLoader;
    }

    /**
     * 新建类加载器（替换旧的）
     */
    public DynamicClassLoader newClassLoader() {
        return new DynamicClassLoader(parentClassLoader);
    }

    /**
     * 获取代码文件管理器
     *
     * @since 1.3.9
     */
    public StandardJavaFileManager getStandardFileManager() {
        return standardFileManager;
    }

    /**
     * 添加类路径
     *
     * @param classPath 类路径
     * @since 1.3.9
     */
    public DynamicCompiler addClassPath(File classPath) throws IOException {
        Iterable<? extends File> locations = standardFileManager.getLocation(StandardLocation.CLASS_PATH);

        List<File> classpaths = StreamSupport.stream(locations.spliterator(), false)
                .collect(Collectors.toList());

        classpaths.add(classPath);

        standardFileManager.setLocation(StandardLocation.CLASS_PATH, classpaths);
        return this;
    }

    /**
     * 添加源码
     *
     * @param className 类名
     * @param source    源码
     */
    public DynamicCompiler addSource(String className, String source) {
        addSource(new StringSource(className, source));
        return this;
    }

    /**
     * 添加源码
     *
     * @param javaFileObject 文件对象
     */
    public DynamicCompiler addSource(JavaFileObject javaFileObject) {
        compilationUnits.add(javaFileObject);
        return this;
    }

    /**
     * 重置
     */
    public void reset() {
        dynamicClassLoader = null;
    }

    /**
     * 编译
     */
    public void compile() {
        errors.clear();
        warnings.clear();

        JavaFileManager fileManager = new DynamicJavaFileManager(standardFileManager, getClassLoader());

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, collector, options, null,
                compilationUnits);

        try {

            if (!compilationUnits.isEmpty()) {
                boolean result = task.call();

                if (!result || collector.getDiagnostics().size() > 0) {

                    for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
                        switch (diagnostic.getKind()) {
                            case NOTE:
                            case MANDATORY_WARNING:
                            case WARNING:
                                warnings.add(diagnostic);
                                break;
                            case OTHER:
                            case ERROR:
                            default:
                                errors.add(diagnostic);
                                break;
                        }

                    }

                    if (!errors.isEmpty()) {
                        throw new DynamicCompilerException("Compilation Error", errors);
                    }
                }
            }
        } catch (Throwable e) {
            throw new DynamicCompilerException(e, errors);
        } finally {
            compilationUnits.clear();
        }

        //编译后需要再调用：getClassLoader().prepareClasses(); 才能使用类
    }

    /**
     * 构建
     */
    public void build() {
        compile();
        getClassLoader().prepareClasses();
    }

    private List<String> diagnosticToString(List<Diagnostic<? extends JavaFileObject>> diagnostics) {

        List<String> diagnosticMessages = new ArrayList<String>();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            diagnosticMessages.add(
                    "line: " + diagnostic.getLineNumber() + ", message: " + diagnostic.getMessage(Locale.US));
        }

        return diagnosticMessages;

    }

    public List<Diagnostic<? extends JavaFileObject>> getOriginalErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<Diagnostic<? extends JavaFileObject>> getOriginalWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public List<String> getErrors() {
        return diagnosticToString(errors);
    }

    public List<String> getWarnings() {
        return diagnosticToString(warnings);
    }
}