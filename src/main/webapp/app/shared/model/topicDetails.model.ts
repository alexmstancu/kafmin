export interface ITopicDetails {
  name?: string;
  internal?: boolean;
  partitions?: number;
}

export class TopicDetails implements ITopicDetails {
  constructor(
    public name?: string,
    public internal?: boolean,
    public partitions?: number
  ) {}
}
