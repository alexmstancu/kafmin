import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICluster } from '@/shared/model/cluster.model';
import ClusterService from './cluster.service';

@Component
export default class ClusterDetails extends Vue {
  @Inject('clusterService') private clusterService: () => ClusterService;
  public cluster: ICluster = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterId) {
        vm.retrieveCluster(to.params.clusterId);
      }
    });
  }

  public retrieveCluster(clusterId) {
    this.clusterService()
      .find(clusterId)
      .then(res => {
        this.cluster = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
