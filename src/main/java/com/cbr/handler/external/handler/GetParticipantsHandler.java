package com.cbr.handler.external.handler;


import com.cbr.handler.external.constant.BpmnErrorConstant;
import com.cbr.handler.external.constant.ProcessVariableConstant;
import com.cbr.handler.external.model.Participant;
import com.cbr.handler.external.service.ParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;

@Configuration
@ExternalTaskSubscription("get-participants")
@Slf4j
public class GetParticipantsHandler implements ExternalTaskHandler {

    private final ParticipantService participantService;

    public GetParticipantsHandler(ParticipantService participantService) {
        this.participantService = participantService;
    }


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            List<Participant> participants = participantService.get();
            HashMap<String, Object> variableMap = new HashMap<>();
            variableMap.put(ProcessVariableConstant.PARTICIPANTS, participants);
            externalTaskService.complete(externalTask, variableMap);
            log.debug("get-participants completed");
        } catch (Exception e) {
            e.printStackTrace();
            externalTaskService.handleBpmnError(externalTask, BpmnErrorConstant.EXTERNAL_TASK_ERROR);
        }
    }
}
