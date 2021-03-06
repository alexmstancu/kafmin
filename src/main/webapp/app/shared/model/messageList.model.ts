import { ICluster } from './cluster.model';
import { IMessage } from './message.model';

export interface IMessageList {
    cluster?: ICluster;
    topic?: string;
    messages?: IMessage[];
    partitionsCount?: number;
  }
  
  export class MessageList implements IMessageList {
    constructor(
        public cluster?: ICluster,
        public topic?: string,
        public messages?: IMessage[],
        public partitionsCount?: number
    ) {}
  }
  