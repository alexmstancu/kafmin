export interface IMessage {
  id?: number;
  key?: string;
  message?: string;
  partition?: number;
  topic?: string;
  date?: Date;
  offest?: number
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public key?: string,
    public message?: string,
    public partition?: number,
    public topic?: string,
    public date?: Date,
    public offest?: number,
  ) {}
}
