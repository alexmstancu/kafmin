export interface ITopic {
  id?: number;
  name?: string;
  isInternal?: boolean;
}

export class Topic implements ITopic {
  constructor(public id?: number, public name?: string, public isInternal?: boolean) {
    this.isInternal = this.isInternal || false;
  }
}
