export interface Task {
    taskId?: number;
    taskName: string;
    description: string;
    dueDateTime: string;
    startDateTime: string;
    status: 'DONE' | 'IN_PROGRESS' | 'TO_DO';
}