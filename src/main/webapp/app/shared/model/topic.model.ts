import { IGenericConfig } from './genericConfig.model';
import { ICluster } from './cluster.model';

export interface ITopic {
  id?: number;
  name?: string;
  isInternal?: boolean;
  configs?: IGenericConfig[];
  cluster?: ICluster;
}

export class Topic implements ITopic {
  constructor(
    public id?: number, 
    public name?: string, 
    public isInternal?: boolean,
    public configs?: IGenericConfig[],
    public cluster?: ICluster
    
    ) {
      this.isInternal = this.isInternal || false;
  }
}
