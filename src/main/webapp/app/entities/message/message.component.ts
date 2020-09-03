import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMessage } from '@/shared/model/message.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import MessageService from './message.service';
import { IMessageList } from '@/shared/model/messageList.model';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Message extends mixins(AlertMixin) {
  @Inject('messageService') private messageService: () => MessageService;
  private removeId: number = null;

  public messageList: IMessageList = null;

  public isFetching = false;

  public partitionFilter = -1;

  public partitionsArray: number[] = [];

  public filteredAndSortedMessages: IMessage[] = [];

  public clusterDbId = -1;
  public topicName = "";


  beforeRouteEnter(to, from, next) {
    console.log("beforeRouteEnter");
    console.log("to" + to);
    console.log("from" + from);
    console.log("next" + next);

    next(vm => {
      if (to.params.clusterDbId && to.params.topicName) {
        vm.clusterDbId = to.params.clusterDbId;
        vm.topicName = to.params.topicName;
        if (to.params.partitionFilter >= 0) {
          vm.partitionFilter = to.params.partitionFilter;
        }
        vm.retrieveAllMessages(to.params.clusterDbId, to.params.topicName);
      }
    });
  }

  public mounted(): void {
    console.log("MOUNTED");
    console.log("MOUNTED cluserId" + this.clusterDbId);
    console.log("MOUNTED topicName" + this.topicName);
    // this.retrieveAllMessages(this.clusterDbId, this.topicName);
  }

  public clear(): void {
    console.log("CLEAR");
    console.log("CLEAR cluserId" + this.clusterDbId);
    console.log("CLEAR topicName" + this.topicName);
    // this.retrieveAllMessages(this.clusterDbId, this.topicName);
  }

  public buildPartitionsArray() {
    for (let i = 0; i < this.messageList.partitionsCount; i++) {
      this.partitionsArray.push(i);
    }
  }

  public retrieveAllMessages(clusterDbId: number, topicName: string): void {
    this.isFetching = true;

    this.messageService()
      .retrieve(clusterDbId, topicName)
      .then(
        res => {
          this.messageList = res.data;
          this.buildPartitionsArray();
          this.messagesFilterByPartitionAndSortByOffsetDescending();
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

  public messagesFilterByPartitionAndSortByOffsetDescending() {
    let messagesFiltertedAndSorted = this.messageList.messages;
    if (this.partitionFilter !== -1) {
      messagesFiltertedAndSorted = messagesFiltertedAndSorted.filter((message) => message.partition === this.partitionFilter)
    }

    this.filteredAndSortedMessages = messagesFiltertedAndSorted.sort(this.offsetDescendingComparator);
  }

  private offsetDescendingComparator = (m1, m2) => {
    if (m1.offset > m2.offset) {
      return -1;
    }

    if (m1.offset < m2.offset) {
        return 1;
    }

    return 0;
  };

  public getFilterText(): string {
    if (this.partitionFilter === -1) {
      return 'Filter by partition: all';
    }
    return 'Filter by partition: ' + this.partitionFilter;
  }

  public resetPartitionFilter() {
    this.partitionFilter = -1;
    this.messagesFilterByPartitionAndSortByOffsetDescending();
  }

  public setPartitionFilter(filter: number) {
    this.partitionFilter = filter;
    this.messagesFilterByPartitionAndSortByOffsetDescending();
  }
}
