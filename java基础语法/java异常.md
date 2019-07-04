# 异常

**什么是异常**

异常(exception)也称为例外, 是一个在程序执行期间发生的事件, 它中断了正在执行的正常的指令流.

**检查异常和非检查异常**

1. 检查异常(checked exception): 编译器要求你必须处理的异常, 需要在程序中显示的捕获或者在方法上用throws进行标注, 如果不进行异常处理, 则程序不能通过编译.

2. 非检查异常(unchecked exception): 编译器认为不需要强制处理的异常, 即使程序有可能有错误, 但是编译器不做检查.


**异常分类**

所有的异常都是Throwable类或者其子类的实例.
Throwable有两大直接字类error, exception.

1. error: 属于非检查异常, 它涵盖了程序不应捕获的异常, 当程序处现error时, 它的执行状态已经无法回复, 需要中止线程甚至中止虚拟机, 被称为程序终结者. 比如VirtualMachineError.

2. exception: 涵盖程序可能需要捕获并处理的异常. 

    2.1 RuntimeException: exception有个特殊的子类-RuntimeException, 表示"程序虽然无法继续运行, 但是还能抢救一下", 它属于非检查异常, 当出现RuntimeException时, java虚拟机自动抛出并自动捕获, 它出现的原因绝大多数是因为程序逻辑问题造成的, 应该从逻辑上去解决并改进代码. 例如NullPointerException, IndexOutOfBoundsException , ClassCastException, ArithmeticException.

    2.2 其他exception: 类型上都属于Exception类及其子类, 这类异常是我们必须手动在代码里捕获处理或者在方法声明中用 throws 关键字标注. 比如IOException, SQLException.

**java虚拟机是如何捕获异常的?**

```
public class Test {
    public static void main(String[] args) {
        try {
            mayThrowException();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void mayThrowException() {
        
    }
}
```
对应的编译字节码:
```
public class Test {
  public Test();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: invokestatic  #2                  // Method mayThrowException:()V
       3: goto          11
       6: astore_1
       7: aload_1
       8: invokevirtual #4                  // Method java/lang/Exception.printStackTrace:()V
      11: return
    Exception table:
       from    to  target type
           0     3     6   Class java/lang/Exception

  public static void mayThrowException();
    Code:
       0: return
}
```

在编译的字节码中, 每个方法都会附带一个异常表, 异常表中的每一个条目代表一个异常处理器, 异常条目由from指针, to指针, target指针以及所捕获的异常类型type构成. 这些类型的值是字节码索引(bytecode index, bci), 用于定位字节码.其中from指针和to指针(不包含)标示了改异常处理器所监控的范围, 例如try代码块所覆盖的范围. target指针则指向异常处理器的起始位置, 例如catch代码的起始位置.

当程序处罚异常时, java虚拟机会自上至下遍历异常表中的所有条目.当触发异常的字节码的索引在某个异常表条目的监控范围内, java会判断所抛出的异常和该条目想要捕获的异常是否匹配. 如果匹配, java虚拟机会将控制流转移至该条目target指针指向的字节码.

如果遍历完所有异常条目, java虚拟机仍未匹配到异常处理器, 那么它会弹出当前方法对应的java栈帧, 并且在调用者(caller)中重复上述操作. 在最坏的情况下, java虚拟机需要遍历当前线程java栈上所有方法的异常表.

finally代码块的编译比较复杂. java编译器的做法是:复制finally代码块的内容, 分别放在try-catch代码块所有正常执行路径以及异常执行路径的出口中.

针对异常执行路径, java编译器会生成一个或多个异常表条目, 监控整个try-catch代码块, 并且捕获所有种类的异常.这些异常条目的target指针将指向另一份复制的finally代码块, 并且在整个finally代码块的最后, java编译器会重新抛出所捕获的异常.


**问题1**
- 问: 如果catch代码块捕获了异常, 并且触发了另一个异常, 那么finally捕获并且重抛的异常是哪个呢?

- 答: 后者. 也就是说原本的异常便会被忽略掉, 这对于代码调试来说十分不利.

**问题2**
- 问: try-catch为什么会降低程序的运行速度?

- 答: 异常实例的构造十分昂贵, 由于在构造异常实例时, java虚拟机便需要生成该异常的栈轨迹(stack trace). 该操作会逐一访问当前线程的java栈帧, 并且记录下各种调试信息, 包括栈帧所指向的方法的名字, 方法所在的类名, 文件名, 以及在代码中的第几行触发该异常.

当然，在生成栈轨迹时，Java 虚拟机会忽略掉异常构造器以及填充栈帧的 Java 方法（Throwable.fillInStackTrace），直接从新建异常位置开始算起。此外，Java 虚拟机还会忽略标记为不可见的 Java 方法栈帧.

**Java 7 的 Supressed 异常以及语法糖**

Java 7 引入了 Supressed 异常来解决问题1。这个新特性允许开发人员将一个异常附于另一个异常之上。因此，抛出的异常可以附带多个异常的信息。

```
Exception exc = new Exception();
exc.addSuppressed(new Exception("Sup. 1"));
exc.addSuppressed(new Exception("Sup. 2"));

System.out.println(Arrays.toString(exc.getSuppressed()));
```

output:
```
[java.lang.Exception: Suppressed 1, java.lang.Exception: Suppressed 2]
```


```
FileInputStream in0 = null;\
FileInputStream in1 = null;
FileInputStream in2 = null;
...
try {
    in0 = new FileInputStream(new File("in0.txt"));
    ...
    try {
        in1 = new FileInputStream(new File("in1.txt"));
        ...
        try {
            in2 = new FileInputStream(new File("in2.txt"));
            ...
        } fnally {
            if (in2 != null) in2.close();
        }
    } fnally {
        if (in1 != null) in1.close();
    }
} fnally {
    if (in0 != null) in0.close();
}
```

Java 7 专门构造了一个名为 try-with-resources 的语法糖，在字节码层面自动使用
Supressed 异常。当然，该语法糖的主要目的并不是使用 Supressed 异常，而是精简资源打开关闭
的用法

```
try (InputStream input = new FileInputStream(fromPath);
        OutputStream output = new FileOutputStream(toPath)) {
        byte[] buffer = new byte[8192];
        int len = -1;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
    }
```
程序可以在 try 关键字后声明并实
例化实现了 AutoCloseable 接口的类，编译器将自动添加对应的 close() 操作。在声明多个
AutoCloseable 实例的情况下，编译生成的字节码类似于上面手工编写代码的编译结果。与手工代
码相比，try-with-resources 还会使用 Supressed 异常的功能，来避免原异常“被消失”。

除了 try-with-resources 语法糖之外，Java 7 还支持在同一 catch 代码块中捕获多种异常。实际实
现非常简单，生成多个异常表条目即可。
```
// 在同一 catch 代码块中捕获多种异常
try {
...
} catch (SomeException | OtherException e) {
...
}
```






