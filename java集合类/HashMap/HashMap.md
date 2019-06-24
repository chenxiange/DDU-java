HashMap
=========
### 成员变量
- size:
HashMap中存放键值对(KV)的数量(键值对(KV)的总和)
- capacity: 
HashMap的容量. capacity就是指HashMap中桶的数量, 默认为16, 一般第一次扩容时会到64, 之后是2倍, 容量都是2的幂.
- loadFactor: 
装载因子. 用来衡量HashMap满的程度, loadFactor默认为0.75, 计算HashMap的实时装在因子的方法为: size/capacity.
- threshold: 
当HashMap的size大于threshold时会执行resize操作,
threshold=capacity*loadFactor
- modCount: 
修改次数, 因为HashMap是线程不安全的,如果在迭代的过程中HashMap被其他线程修改了, modCount的数值就会发生变化, 这个时候expectedModCount和modCount不相等,迭代器就会抛出ConcurrentModificationException异常.

