import {AppEvent, MessageType} from "@/utils/enums";

export default function validateProject(emitter, project) {
    if (project.agents.length === 0) {
        emitter(AppEvent.MESSAGE, {content: "Unable to start SMA without agents", type: MessageType.ERROR});
        return true;
    }

    let hasSameName = false;
    for (let agent in project.agents) {
        for (let anotherAgent in project.agents) {
            if (agent !== anotherAgent && agent.name === anotherAgent.name) {
                hasSameName = true;
                break;
            }
        }
        if (hasSameName) {
            return;
        }
    }

    if (hasSameName) {
        emitter(AppEvent.MESSAGE, {content: "There are agents with the same name", type: MessageType.ERROR});
        return true;
    }

    return false;
}

