package com.auth.ai.model.value;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@StructuredPrompt("法律内容：{{legal}}\n解答用户以下问题：{{question}}")
@Schema(description = "大模型法律提示词")
@Data
public class LawPromptValue {

    @Schema(description = "法律内容")
    private String legal;

    @Schema(description = "用户问题")
    private String question;

}
