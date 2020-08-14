import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import BrokerService from '../broker/broker.service';
import { IBroker, Broker } from '@/shared/model/broker.model';

import AlertService from '@/shared/alert/alert.service';
import { ICluster, Cluster } from '@/shared/model/cluster.model';
import ClusterService from './cluster.service';
import { mixins } from 'vue-class-component';
import AlertMixin from '@/shared/alert/alert.mixin';

const validations: any = {
  cluster: {
    clusterId: {},
    name: {},
    bootstrapServers: {}
  },
};

@Component({
  validations,
})
export default class ClusterUpdate extends mixins(AlertMixin) {
  @Inject('clusterService') private clusterService: () => ClusterService;
  public cluster: ICluster = new Cluster();

  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterId) {
        vm.retrieveCluster(to.params.clusterId);
      } else {
        vm.cluster.brokers = [];
        vm.addBroker();
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
    if (this.cluster.id) {
      this.clusterService()
        .update(this.cluster)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Cluster is updated with identifier ' + param.clusterId;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      if (!this.isValid()) {
        this.isSaving = false;
        return;
      }

      this.clusterService()
        .create(this.cluster)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Cluster was connected with identifier ' + param.clusterId;
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

  public addBroker(): void {
    const broker = new Broker();
    broker.id = 
    this.cluster.brokers.push(broker);
  }

  public isAddBrokerButtonDisabled(): boolean {
    console.log('isDisabled');
    if (!this.cluster.brokers || this.cluster.brokers.length === 0) {
      return true;
    }

    const lastIndex = this.cluster.brokers.length - 1;
    const lastBroker = this.cluster.brokers[lastIndex];
    if (!lastBroker.host || !lastBroker.port) {
      return true;
    }

    return false;
  }

  public isValid(): boolean {
    if (!this.cluster.name) {
      console.log("is not valid name");
      const message = 'You need to provide a name for the new cluster';
      this.alertService().showAlert(message, 'danger');
      this.getAlertFromStore();
      return false;
    }


    const lastIndex = this.cluster.brokers.length - 1;
    if (lastIndex === 0) {
      const lastBroker = this.cluster.brokers[lastIndex];
      if (!lastBroker.host || !lastBroker.port) {
        console.log("is not valid broker");
        const message = 'You need to provide at least one bootstrap broker (host and port).';
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        return false;
      }
    }

    return true;
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
}
