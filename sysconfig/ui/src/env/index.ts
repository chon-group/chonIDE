import {backendUrl as devBackendUrl} from "@/env/env.dev";
import {backendUrl as prodBackendRole} from "@/env/env.prod";

const isDev = process.env.NODE_ENV === 'development';

export const backendUrl = isDev ? devBackendUrl : prodBackendRole;