import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';
import { ITopic, Topic } from '@/shared/model/topic.model';
import TopicService from './topic.service';
import { IGenericConfig } from '@/shared/model/genericConfig.model';

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
  public savedClusterDbId: number = -1;
  public originalConfigsMap: Map<string, IGenericConfig> = new Map();

  public configEditedStyle = {
    'background-color': '#f5e5a4'
  };

  public applyStyleIfEdited(config: IGenericConfig): object {
    if (this.wasEdited(config)) {
      return this.configEditedStyle;
    }
    return {};
  }

  public wasEdited(config: IGenericConfig): boolean {
    return this.originalConfigsMap.get(config.name).value != config.value;
  }

  public cacheOriginalConfigs(configs: IGenericConfig[]) {
    var configsCopy = configs.map(x => Object.assign({}, x));
    return configsCopy.reduce((map, config) => {
      map.set(config.name, config);
      return map;
    }, this.originalConfigsMap);
  }

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterDbId) {
        vm.savedClusterDbId = to.params.clusterDbId
        if (to.params.topicName) {
          vm.retrieveTopic(to.params.clusterDbId, to.params.topicName);
        }
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
    if (this.topic.cluster !== undefined) {
      this.topicService()
        .update(this.topic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'Topic with name ' + param.name + ' was updated.';
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.topicService()
        .create(this.savedClusterDbId, this.topic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A topic was created with name ' + param.name;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveTopic(clusterDbId, topicName): void {
    this.topicService()
      .find(clusterDbId, topicName)
      .then(res => {
        this.topic = res;
        this.cacheOriginalConfigs(this.topic.configs);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}


  public isReadonly(config: IGenericConfig): string {
    if (config.isReadOnly) {
      return "yes";
    }
    return "no";
  }

  public getPartitionsCount(): number {
    if (this.topic.partitions) {
      return this.topic.partitions.length;
    }
    return 0;
  }

  public isInternal(): string {
    if (this.topic.isInternal) {
      return 'yes';
    }
    return 'no';
  }

  public getNonReadOnlyConfigs(): IGenericConfig[] {
    return this.topic.configs.filter(config => !config.isReadOnly)
  }
}
