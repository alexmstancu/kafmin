export interface IMessage {
  id?: number;
  key?: string;
  message?: string;
  partition?: number;
}

export class Message implements IMessage {
  constructor(public id?: number, public key?: string, public message?: string, public partition?: number) {}
}
