import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import ClusterService from '../cluster/cluster.service';
import { ICluster } from '@/shared/model/cluster.model';

import AlertService from '@/shared/alert/alert.service';
import { IBroker, Broker } from '@/shared/model/broker.model';
import BrokerService from './broker.service';

const validations: any = {
  broker: {
    brokerId: {},
    host: {},
    port: {},
    isController: {},
  },
};

@Component({
  validations,
})
export default class BrokerUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('brokerService') private brokerService: () => BrokerService;
  public broker: IBroker = new Broker();

  @Inject('clusterService') private clusterService: () => ClusterService;

  public clusters: ICluster[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.brokerId) {
        vm.retrieveBroker(to.params.brokerId);
      }
      vm.initRelationships();
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
    if (this.broker.id) {
      this.brokerService()
        .update(this.broker)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Broker is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.brokerService()
        .create(this.broker)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Broker is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveBroker(brokerId): void {
    this.brokerService()
      .find('dummyClusterId', brokerId)
      .then(res => {
        this.broker = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.clusterService()
      .retrieve()
      .then(res => {
        this.clusters = res.data;
      });
  }
}
