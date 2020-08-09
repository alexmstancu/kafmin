import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBroker } from '@/shared/model/broker.model';
import BrokerService from './broker.service';
import { IGenericConfig } from '@/shared/model/genericConfig.model';

@Component
export default class BrokerDetails extends Vue {
  @Inject('brokerService') private brokerService: () => BrokerService;
  public broker: IBroker = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clusterId && to.params.brokerId) {
        vm.retrieveBroker(to.params.clusterId, to.params.brokerId);
      }
    });
  }

  public retrieveBroker(clusterDbId, brokerId) {
    this.brokerService()
      .find(clusterDbId, brokerId)
      .then(res => {
        this.broker = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }

  public isReadonly(config: IGenericConfig): string {
    if (config.isReadOnly) {
      return "yes";
    }
    return "no";
  }
}
