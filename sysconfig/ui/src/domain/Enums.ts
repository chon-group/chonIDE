export enum MessageType {
    ERROR,
    SUCCESS,
    WARNING
}

export enum Key {
    ENTER = "Enter",
    BACKSPACE = "Backspace",
    BRACKET_RIGHT = "{",
    SQUARE_BRACKET_RIGHT = "[",
    PARENTESIS_RIGHT = "(",
    TAB = "Tab",
    BACKQUOTE = "'",
    DOUBLE_BACKQUOTE = "\""
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

