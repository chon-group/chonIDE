export enum MessageType {
    ERROR,
    SUCCESS,
    WARNING
}

export enum Key {
    ENTER = "Enter"
}

export enum AgentType {
    ARGO = "Argo",
    JASON = "Jason",
    COMMUNICATOR = "Communicator"
}

export const AgentTypes = [AgentType.ARGO, AgentType.JASON, AgentType.COMMUNICATOR];

export enum AppEvent {
    MESSAGE = "message"
}

