package com.baidu.shunba.controller.advice;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = AppException.class)
    public ResultVo<String> appExceptionHandler(AppException exception, HttpServletResponse response) {
        // log.error(" ========== 业务异常: {}", exception.getMessage());
        response.setStatus(HttpStatus.OK.value());
        return ResultVo.error(exception);
    }

    @ResponseBody
    @ExceptionHandler(value = PersistenceException.class)
    public ResultVo<String> sqlException(PersistenceException exception) {
        log.error(" ========== 数据库异常: ", exception);
        return ResultVo.error(ResultEnum.DB_EXCEPTION.getCode(), exception.getMessage(), exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public ResultVo<String> throwable(Throwable exception) {
        log.error(" ========== 操作错误: ", exception);
        return ResultVo.error(exception);
    }
}
