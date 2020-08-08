import { IBroker } from '@/shared/model/broker.model';
import { ITopicDetails } from '@/shared/model/topicDetails.model'

export interface ICluster {
  id?: number;
  clusterId?: string;
  name?: string;
  bootstrapServers?: string;
  topicsCount?: number;
  partitionsCount?: number;
  brokers?: IBroker[];
  topics?: ITopicDetails[];
}

export class Cluster implements ICluster {
  constructor(
    public id?: number,
    public clusterId?: string,
    public name?: string,
    public bootstrapServers?: string,
    public topicsCount?: number,
    public partitionsCount?: number,
    public brokers?: IBroker[],
    public topics?: ITopicDetails[]
  ) {}
}
