package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.DynamicCompiler;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author noear 2025/2/5 created
 */
public class Case13 {
    @Test
    public void test() throws Exception {
        final DynamicCompiler dynamicCompiler = new DynamicCompiler(new MyClassLoader(null));

        final File classPath = new File(System.getProperty("user.dir"), "tmp");

        // 启用这行代码，能让 A 类找到 B 类，但仍然进不到 MyClassLoader.loadClass()
        // 因为 JavaCompiler 内部走的 Launcher.AppClassLoader 去加载类，并没有进入 MyClassLoader.loadClass()
        dynamicCompiler.addClassPath(classPath);

        final File codeSourceFile = new File(classPath, "test/A.java");
        dynamicCompiler.addSource(new JavaFileSource(codeSourceFile.toURI())).build();

        final Class<?> clazz = dynamicCompiler.getClassLoader().loadClass("test.A");
        System.out.printf("clazz: %s\n", clazz);
    }

    private static class MyClassLoader extends ClassLoader {
        public MyClassLoader(ClassLoader parent) {
            // 直接指定 parent 为 null,不使用默认的 ContextClassLoader
            super(null);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            System.out.println("Trying to load: " + name);
            // 首先尝试自己加载
            try {
                return findClass(name);
            } catch (ClassNotFoundException e) {
                return super.loadClass(name, resolve);
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("Finding class: " + name);
            // 实现类查找逻辑
            return super.findClass(name);
        }

    }

    public static class JavaFileSource extends SimpleJavaFileObject {

        public JavaFileSource(URI codeSourceFile) {
            super(codeSourceFile, Kind.SOURCE);
        } // __constructor

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return new String(Files.readAllBytes(Paths.get(this.uri)), StandardCharsets.UTF_8);
        } // getCharContent()
    }
}
