import { IGenericConfig } from './genericConfig.model';
import { ICluster } from './cluster.model';
import { ITopicPartition } from './topicPartition.model';

export interface ITopic {
  id?: number;
  name?: string;
  isInternal?: boolean;
  configs?: IGenericConfig[];
  cluster?: ICluster;
  partitions?: ITopicPartition[];
  numPartitions?: number;
  replicationFactor?: number;
}

export class Topic implements ITopic {
  constructor(
    public id?: number, 
    public name?: string, 
    public isInternal?: boolean,
    public configs?: IGenericConfig[],
    public cluster?: ICluster,
    public partitions?: ITopicPartition[],
    public numPartitions?: number,
    public replicationFactor?: number
    ) {
      this.isInternal = this.isInternal || false;
  }
}
