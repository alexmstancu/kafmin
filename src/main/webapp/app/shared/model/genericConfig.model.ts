export interface IGenericConfig {
    name?: string;
    value?: string;
    isReadOnly?: boolean;
  }

  export class GenericConfig implements IGenericConfig {
    constructor(
      public name?: string,
      public value?: string,
      public isReadOnly?: boolean
    ) {}
  }
