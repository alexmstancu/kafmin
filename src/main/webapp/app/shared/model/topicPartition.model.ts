import { IBroker } from './broker.model';

export interface ITopicPartition {
    id?: number;
    leader?: IBroker;
    replicas?: IBroker[];
}

export class TopicPartition implements ITopicPartition {
    constructor(
        public id?: number,
        public leader?: IBroker,
        public replicas?: IBroker[]
    ) {

    }
}
