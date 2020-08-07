import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import BrokerService from '../broker/broker.service';
import { IBroker, Broker } from '@/shared/model/broker.model';

import AlertService from '@/shared/alert/alert.service';
import { ICluster, Cluster } from '@/shared/model/cluster.model';
import ClusterService from './cluster.service';

const validations: any = {
  cluster: {
    clusterId: {},
    name: {},
    bootstrapServers: {},
  },
};

@Component({
  validations,
})
export default class ClusterUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('clusterService') private clusterService: () => ClusterService;
  public cluster: ICluster = new Cluster();

  @Inject('brokerService') private brokerService: () => BrokerService;

  public brokers: IBroker[] = [];
  public initialBroker: IBroker = new Broker();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterId) {
        vm.retrieveCluster(to.params.clusterId);
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
    if (this.cluster.id) {
      this.clusterService()
        .update(this.cluster)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Cluster is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.addBroker(this.initialBroker.host, this.initialBroker.port)
      this.cluster.brokers = this.brokers;

      this.clusterService()
        .create(this.cluster)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Cluster is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveCluster(clusterId): void {
    this.clusterService()
      .find(clusterId)
      .then(res => {
        this.cluster = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.brokerService()
      .retrieve()
      .then(res => {
        this.brokers = res.data;
      });
  }

  public addBroker(host, port) : void {
    const broker = new Broker();
    broker.host = host;
    broker.port = port;
    this.brokers.push(broker);
  }
}
