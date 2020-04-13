package com.xue.ipass.aspect;
import com.xue.ipass.annotation.AddLog;
import com.xue.ipass.entity.Admin;
import com.xue.ipass.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Aspect
@Configuration
public class LogAspect {

    @Resource
    HttpSession session;

    //@Around("@annotation(com.xue.ipass.annotation.AddLog)")
    /**自定义拦截*/
    public Object addLogs(ProceedingJoinPoint joinPoint){

        //谁   时间   操作   是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //时间
        /*Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formats = format.format(date);*/

        //操作   哪个方法
        String methodName = joinPoint.getSignature().getName();

        //获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取方法上的注解
        AddLog addLog = method.getAnnotation(AddLog.class);

        //获取注解中属性的值   value
        String methodDes= addLog.value();


        //放行方法
        try {
            Object proceed = joinPoint.proceed();

            String message="success";

            //System.out.println("管理员："+admin.getUsername()+"--时间："+new Date()+"--操作："+methodName+"--状态："+message);

            /**--------------有问题没解决**/
            Log log = new Log(UUID.randomUUID().toString(),admin.getUsername(),new Date(),methodDes+"("+methodName+")",message);
            System.out.println("数据库插入"+log);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message="error";
            return null;
        }
    }
    /**元注解拦截*/
    //@Around("execution(* com.xue.ipass.serviceImpl.*.*(..) ) && !execution(* com.xue.ipass.serviceImpl.*.query*(..))")
    public Object addLog(ProceedingJoinPoint joinPoint){

        //谁   时间   操作   是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formats = format.format(date);

        //操作   哪个方法
        String methodName = joinPoint.getSignature().getName();

        //放行方法
        try {
            Object proceed = joinPoint.proceed();

            String message="success";

            System.out.println("管理员："+admin.getUsername()+"--时间："+formats+"--操作："+methodName+"--状态："+message);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message="error";
            return null;
        }
    }

}

