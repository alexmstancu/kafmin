import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICluster } from '@/shared/model/cluster.model';
import ClusterService from './cluster.service';
import TopicService from '../topic/topic.service';
import { ITopic } from '@/shared/model/topic.model';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ClusterDetails extends Vue {
  @Inject('clusterService') private clusterService: () => ClusterService;
  @Inject('topicService') private topicService: () => TopicService;
  @Inject('alertService') private alertService: () => AlertService;
  public cluster: ICluster = {};
  public removedTopicName: string = '';
  
  public isFetchingTopics: boolean = false;

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

  public prepareRemove(instance: ITopic): void {
    console.log("o sa il sterg pe dansul " + instance.name)
    this.removedTopicName = instance.name;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTopic(): void {
    console.log("in remove topic stergem pe " + this.removedTopicName)
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
        this.$router.go(0)
      });
  }

}
