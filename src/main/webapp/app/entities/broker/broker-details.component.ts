import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBroker } from '@/shared/model/broker.model';
import BrokerService from './broker.service';

@Component
export default class BrokerDetails extends Vue {
  @Inject('brokerService') private brokerService: () => BrokerService;
  public broker: IBroker = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.brokerId) {
        vm.retrieveBroker(to.params.brokerId);
      }
    });
  }

  public retrieveBroker(brokerId) {
    this.brokerService()
      .find(brokerId)
      .then(res => {
        this.broker = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
