  import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICluster } from '@/shared/model/cluster.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import ClusterService from './cluster.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Cluster extends mixins(AlertMixin) {
  @Inject('clusterService') private clusterService: () => ClusterService;
  private removeId: number = null;
  private removedClusterId: string = null;

  public clusters: ICluster[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllClusters();
  }

  public clear(): void {
    this.retrieveAllClusters();
  }

  public retrieveAllClusters(): void {
    this.isFetching = true;

    this.clusterService()
      .retrieve()
      .then(
        res => {
          this.clusters = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: ICluster): void {
    this.removeId = instance.id;
    this.removedClusterId = instance.clusterId
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCluster(): void {
    this.clusterService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A cluster was disconnected with identifier ' + this.removedClusterId;
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllClusters();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public getBrokersCount(cluster: ICluster): number {
    if (cluster.brokers == null || cluster.brokers.length === 0) {
      return 0;
    }
    return cluster.brokers.length;
  }
}
