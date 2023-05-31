package com.rickross;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.workflowservice.v1.DeleteWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DeleteWorkflowExecutionResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Required arguments: namespace workflow-id run-id");
            System.exit(0);
        }

        String namespace = args[0];
        String workflowId = args[1];
        String runId = args[2];
        System.out.format("Attempting to delete workflow history in namespace %s, workflow ID %s, run ID %s",
                namespace, workflowId, runId);
        try {

            // This code assumes you are deleting a workflow running locally.

            WorkflowExecution workflow =
                    WorkflowExecution.newBuilder()
                            .setRunId(runId)
                            .setWorkflowId(workflowId)
                            .build();

            DeleteWorkflowExecutionRequest deleteRequest =
                    DeleteWorkflowExecutionRequest.newBuilder()
                            .setNamespace(namespace)
                            .setWorkflowExecution(workflow)
                            .build();

            WorkflowServiceStubsOptions serviceStubsOptions =
                    WorkflowServiceStubsOptions.newBuilder().build();
            WorkflowServiceStubs workflowServiceStubs =
                    WorkflowServiceStubs.newServiceStubs(serviceStubsOptions);

            DeleteWorkflowExecutionResponse response =
                    workflowServiceStubs.blockingStub().deleteWorkflowExecution(deleteRequest);

            System.out.format("Complete. Response: %s", response);
        } catch (Exception ex) {
            System.out.println("Exception occurred " + ex);
            ex.printStackTrace();
        }
    }
}
