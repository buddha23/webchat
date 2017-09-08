package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.annotation.OperateRecord;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: dhc
 * Date: 2016-10-12 15:48
 */
@RestController
@RequestMapping("/tool")
public class AdmToolController extends AdmController {
    @Autowired
    private DocService docService;

    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * 给未生成水印文件的PDF文档添加水印
     * 并对未生成50张图片的文档作图片补全
     *
     * @return 补全水印的PDF
     */
    @OperateRecord("未生成水印文件的PDF文档添加水印")
    @RequestMapping("/doc_imgs_wm")
    public ResultOut completionPdfWatermarkGet() {
        final String DOC_COMPLETION_PROCESS = "DOC_COMPLETION_PROCESS";
        if (context.getAttribute(DOC_COMPLETION_PROCESS) != null) {
            return new ResultOut(M.E10002, "文档图片和PDF水印补全工作进行中……！");
        }
        context.setAttribute(DOC_COMPLETION_PROCESS, "Doing……");
        taskExecutor.execute(() -> {
            try {
                docService.completionImagesAndPdfWatermarkFile();
            } finally {
                context.removeAttribute(DOC_COMPLETION_PROCESS);
            }
        });
        return new ResultOut(M.E10002, "文档图片和PDF水印补全的任务已启动！");
    }

    /**
     * 给指定的PDF文档添加水印
     * 并对指定未生成50张图片的文档作图片补全
     *
     * @return 补全水印的PDF
     */
    @OperateRecord("给指定的PDF文档添加水印")
    @RequestMapping("/doc_imgs_wm/{id}")
    public Doc completionPdfWatermarkGet(@PathVariable long id) {
        return docService.completionImagesAndPdfWatermarkFile(id);
    }

}
