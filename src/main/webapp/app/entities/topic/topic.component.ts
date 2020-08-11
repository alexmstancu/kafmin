import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITopic } from '@/shared/model/topic.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import TopicService from './topic.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Topic extends mixins(AlertMixin) {
  @Inject('topicService') private topicService: () => TopicService;
  private removeId: number = null;

  public topics: ITopic[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTopics();
  }

  public clear(): void {
    this.retrieveAllTopics();
  }

  public retrieveAllTopics(): void {
    this.isFetching = true;

    this.topicService()
      .retrieve()
      .then(
        res => {
          this.topics = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  // not used
  public prepareRemove(instance: ITopic): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  // not used
  public removeTopic(): void {
    this.topicService()
      .delete(1, "dummy")
      .then(() => {
        const message = 'A Topic is deleted with identifier ' + this.removeId;
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllTopics();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
