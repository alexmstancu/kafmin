import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';
import { ITopic, Topic } from '@/shared/model/topic.model';
import TopicService from './topic.service';

const validations: any = {
  topic: {
    name: {},
    isInternal: {},
    numPartitions: {},
    replicationFactor: {}
  },
};

@Component({
  validations,
})
export default class TopicUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('topicService') private topicService: () => TopicService;
  public topic: ITopic = new Topic();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterDbId && to.params.topicId) {
        vm.retrieveTopic(to.params.clusterDbId, to.params.topicId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.topic.cluster) {
      this.topicService()
        .update(this.topic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Topic is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.topicService()
        .create(this.topic.cluster.id, this.topic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Topic is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveTopic(clusterDbId, topicName): void {
    this.topicService()
      .find(clusterDbId, topicName)
      .then(res => {
        this.topic = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
