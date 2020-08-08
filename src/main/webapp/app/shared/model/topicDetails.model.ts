export interface ITopicDetails {
  name?: string;
  isInternal?: boolean;
  partitions?: number;
}

export class TopicDetails implements ITopicDetails {
  constructor(
    public name?: string,
    public isInternal?: boolean,
    public partitions?: number
  ) {}
}
