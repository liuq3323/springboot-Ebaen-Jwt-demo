package com.example.exceptionhandler;

import com.example.util.Result;
import com.example.util.ServiceRuntimeException;
import org.apache.http.HttpStatus;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String DEFAULT_ERROR_VIEW = "error.html";

//    @Autowired
//    private MultipartProperties multipartProperties;

    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("defaultErrorHandler ", e.getMessage());
        return Result.fail(null, e.getMessage());
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result defaultErrorHandler(Throwable e, HttpServletResponse response) throws Exception {
        //业务类型异常，异常消息会被包装到response的json的message中
        if(e instanceof ServiceRuntimeException){
            ServiceRuntimeException ex = (ServiceRuntimeException) e;
            if(StringUtils.isEmpty(ex.getCode())){
                return Result.fail(null, e.getMessage());
            }
            return Result.fail(null, ex.getCode(),ex.getMessage());
        }

        //如果采用hibernate的validate框架，需要处理validate框架抛出的异常
        if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = validException.getBindingResult();
            if(bindingResult == null){
                logger.error("no bindingResult found ", e);
                return Result.fail(null, "系统异常");
            }
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if(allErrors == null || allErrors.size() == 0){
               // logger.error("no error message found ", e);
                return Result.fail(null, "系统异常");
            }
            ObjectError objectError = allErrors.get(0);
            return Result.fail(null, objectError.getDefaultMessage());
        }

        //检查文件上传异常
        if(e instanceof MultipartException){
            if("Current request is not a multipart request".equals(e.getMessage())){
                return Result.fail(null, "文件不可以为空");
            } else if(e.getMessage().contains("FileSizeLimitExceededException")){
                return Result.fail(null, String.format("文件大小不能超过 %s",""));
            } else if(e.getMessage().contains("SizeLimitExceededException")){
                return Result.fail(null, String.format("超过单次请求提交文件大小限制 %s",""));
            }
        }

        //请求方法不支持
        if(e instanceof HttpRequestMethodNotSupportedException){
            response.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
            return Result.fail(null, e.getMessage());
        }
        //请求参数不正确
        if(e instanceof MissingServletRequestParameterException){
          response.setStatus(HttpStatus.SC_OK);
          return Result.fail(null, e.getMessage());
        }

        //如果不是业务类型异常，为未知异常，返回“系统异常”提示信息，日志中打印异常堆栈
        //logger.error("system error ", e);
        return Result.fail(null, "系统异常");
    }

}
