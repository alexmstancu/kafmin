import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICluster } from '@/shared/model/cluster.model';
import ClusterService from './cluster.service';
import TopicService from '../topic/topic.service';
import { ITopic } from '@/shared/model/topic.model';
import AlertService from '@/shared/alert/alert.service';
import { ITopicDetails, TopicDetails } from '@/shared/model/topicDetails.model';

@Component
export default class ClusterDetails extends Vue {
  @Inject('clusterService') private clusterService: () => ClusterService;
  @Inject('topicService') private topicService: () => TopicService;
  @Inject('alertService') private alertService: () => AlertService;
  public cluster: ICluster = {};
  public removedTopicName = '';
  public isFetchingTopics = false;
  public sortedTopics: ITopicDetails[];

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterId) {
        vm.retrieveCluster(to.params.clusterId);
      }
    });
  }

  public retrieveCluster(clusterId) {
    this.isFetchingTopics = true;
    this.clusterService()
      .find(clusterId)
      .then(res => {
        this.cluster = res;
        this.sortedTopics = this.getTopicsSortedByNameAscendingAndInternalDescening(this.cluster.topics);
        this.isFetchingTopics = false;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getTopicsCount(): number {
    if (this.cluster.topicsCount == null) {
      return 0;
    }
    return this.cluster.topicsCount;
  }

  public getPartitionsCount(): number {
    if (this.cluster.partitionsCount == null) {
      return 0;
    }
    return this.cluster.partitionsCount;
  }

  public isController(broker): string {
    if (broker.isController) {
      return 'yes';
    }
    return 'no';
  }

  public isInternal(topic): string {
    if (topic.internal) {
      return 'yes';
    }
    return 'no';
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public prepareRemove(instance: ITopicDetails): void {
    this.removedTopicName = instance.name;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTopic(): void {
    this.topicService()
      .delete(this.cluster.id, this.removedTopicName)
      .then(() => {
        const message = 'A topic was deleted with name ' + this.removedTopicName;
        this.alertService().showAlert(message, 'danger');
        // this.getAlertFromStore();
        this.removedTopicName = null;
        // this.retrieveCluster(this.cluster.clusterId)
        // this.retrieveAllTopics();
        this.closeDialog();
        this.$router.go(0);
      });
  }

  public getTopicsSortedByNameAscendingAndInternalDescening(topics: ITopicDetails[]) {
    return topics.sort(this.nameAlphabeticallyComparator);
  }

  private nameAlphabeticallyComparator = (t1, t2) => {
    if (t1.internal) {
      console.log(t1.name + 'is internal first')
      if (t2.internal) {
        console.log(t2.name + 'is internal second')
        return t1.name.localeCompare(t2.name);
      } else {
        return -1;
      }
    }

    if (t2.internal) {
      if (t1.internal) {
        return t2.name.localeCompare(t1.name);
      } else {
        return 1;
      }
    }
    

    return t1.name.localeCompare(t2.name);
  };
}
