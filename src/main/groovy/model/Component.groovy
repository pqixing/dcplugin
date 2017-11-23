package model
/**
 * 组件化相关配置
 */
public class Component{
    String luanchActivity
    String application








    static Component create(Closure call){
        def m = new Component()
        call.delegate = m
        call.setResolveStrategy(Closure.DELEGATE_FIRST)
        call()
        return m
    }
}