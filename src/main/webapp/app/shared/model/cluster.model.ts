import { IBroker } from '@/shared/model/broker.model';

export interface ICluster {
  id?: number;
  clusterId?: string;
  name?: string;
  brokers?: IBroker[];
}

export class Cluster implements ICluster {
  constructor(public id?: number, public clusterId?: string, public name?: string, public brokers?: IBroker[]) {}
}
