import { ICluster } from '@/shared/model/cluster.model';

export interface IBroker {
  id?: number;
  brokerId?: string;
  host?: string;
  port?: number;
  isController?: boolean;
  cluster?: ICluster;
}

export class Broker implements IBroker {
  constructor(
    public id?: number,
    public brokerId?: string,
    public host?: string,
    public port?: number,
    public isController?: boolean,
    public cluster?: ICluster
  ) {
    this.isController = this.isController || false;
  }
}
