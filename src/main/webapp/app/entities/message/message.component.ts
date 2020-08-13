import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMessage } from '@/shared/model/message.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import MessageService from './message.service';
import { IMessageList } from '@/shared/model/messageList.model';
import { da } from 'date-fns/locale';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Message extends mixins(AlertMixin) {
  @Inject('messageService') private messageService: () => MessageService;
  private removeId: number = null;

  public messageList: IMessageList;

  public isFetching = false;

  public partitionFilter = -1;

  public partitionsArray: number[] = [];

  public buildPartitionsArray() {
    for (let i = 0; i < this.messageList.partitionsCount; i++) {
      this.partitionsArray.push(i);
    }
  }

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterDbId && to.params.topicName) {
        console.log('msg component beforeRouteEnter' + to.params.clusterDbId + to.params.topicName)
        vm.retrieveAllMessages(to.params.clusterDbId, to.params.topicName);
      }
    });
  }

  // public mounted(): void {
  //   this.retrieveAllMessages();
  // }

  // public clear(): void {
  //   this.retrieveAllMessages();
  // }

  public retrieveAllMessages(clusterDbId: number, topicName: string): void {
    this.isFetching = true;

    this.messageService()
      .retrieve(clusterDbId, topicName)
      .then(
        res => {
          this.messageList = res.data;
          this.buildPartitionsArray();
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
        // this.retrieveAllMessages();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public getPrettyDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleString();
  }


  public previousState() {
    this.$router.go(-1);
  }

  public messagesSortedByOffsetDescending(): IMessage[] {
    return this.messageList.messages.sort((m1, m2) => {
      if (m1.offset > m2.offset) {
        return -1;
      }

      if (m1.offset < m2.offset) {
          return 1;
      }

      return 0;
    });
  }

  public getTotalMessagesNumber(): number {
    if (this.partitionFilter === -1) {
      return this.messageList.messages.length;
    }
    return 0;
  }

  public getFilterText(): string {
    if (this.partitionFilter == -1) {
      return 'Filter by partition: all';
    }
    return 'Filter by partition: ' + this.partitionFilter;
  }

}
