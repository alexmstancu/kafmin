import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMessage } from '@/shared/model/message.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import MessageService from './message.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Message extends mixins(AlertMixin) {
  @Inject('messageService') private messageService: () => MessageService;
  private removeId: number = null;

  public messages: IMessage[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMessages();
  }

  public clear(): void {
    this.retrieveAllMessages();
  }

  public retrieveAllMessages(): void {
    this.isFetching = true;

    this.messageService()
      .retrieve()
      .then(
        res => {
          this.messages = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IMessage): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMessage(): void {
    this.messageService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Message is deleted with identifier ' + this.removeId;
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllMessages();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
