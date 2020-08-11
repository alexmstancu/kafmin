import { ICluster } from '@/shared/model/cluster.model';
import { IGenericConfig } from './genericConfig.model';

export interface IBroker {
  id?: number;
  brokerId?: string;
  host?: string;
  port?: number;
  isController?: boolean;
  cluster?: ICluster;
  configs?: IGenericConfig[];
}

export class Broker implements IBroker {
  constructor(
    public id?: number,
    public brokerId?: string,
    public host?: string,
    public port?: number,
    public isController?: boolean,
    public cluster?: ICluster,
    public configs?: IGenericConfig[]
  ) {
    this.isController = this.isController || false;
  }
}
