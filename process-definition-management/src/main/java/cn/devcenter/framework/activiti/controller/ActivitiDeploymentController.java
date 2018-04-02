package cn.devcenter.framework.activiti.controller;

import cn.devcenter.framework.starter.web.annotation.RestController;
import cn.devcenter.model.result.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@Slf4j
public class ActivitiDeploymentController {

    private static final Long MB = 2L;

    private static final Long MAX_FILE_SIZE = MB * 1024 * 1024;

    private static final String ACTIVITI_SPRING_PATH = "processes/";

    private static final String PROCESS_DEFINITION_FILE_SUFFIX = ".bpmn20.xml";

    private final ProcessEngine processEngine;

    @Autowired
    public ActivitiDeploymentController(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @RequestMapping(value = "/deploy-with-file", method = RequestMethod.POST)
    public AjaxResult<String> deploy(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            log.error("No file was accepted. Please notice that the param name is 'file'");
            return AjaxResult.newInstance(String.class).fail("No file was accepted. Please notice that the param name is 'file'");
        }
        if (MAX_FILE_SIZE < multipartFile.getSize()) {
            log.error("Uploaded file size is too large. File size should be less than " + MB + "MB");
            return AjaxResult.newInstance(String.class).fail("Uploaded file size is too large. File size should be less than " + MB + "MB");
        }

        String relativePath = ACTIVITI_SPRING_PATH + multipartFile.getOriginalFilename().replace(PROCESS_DEFINITION_FILE_SUFFIX, "") + "." + System.currentTimeMillis() + PROCESS_DEFINITION_FILE_SUFFIX;
        String filePath = getClass().getClassLoader().getResource("").getPath() + relativePath;
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.getParentFile().mkdirs();
        }
        try {
            byte[] bytes = multipartFile.getBytes();
            BufferedOutputStream buffStream =
                    new BufferedOutputStream(new FileOutputStream(desFile));
            buffStream.write(bytes);
            buffStream.close();
        } catch (Exception e) {
            log.error("", e);
            return AjaxResult.newInstance(String.class).fail("You failed to upload " + filePath);
        }

        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("流程定义")//添加部署名称
                .addClasspathResource(relativePath)//从classpath的资源中加载，一次只能加载一个文件
                .deploy();//完成部署
        log.info("Deployment ID：" + deployment.getId());
        log.info("Deployment Key：" + deployment.getKey());
        return AjaxResult.newInstance(String.class).success("Deployed successfully", deployment.getId());
    }

}
