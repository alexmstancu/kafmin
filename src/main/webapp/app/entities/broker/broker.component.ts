import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBroker } from '@/shared/model/broker.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import BrokerService from './broker.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Broker extends mixins(AlertMixin) {
  @Inject('brokerService') private brokerService: () => BrokerService;
  private removeId: number = null;

  public brokers: IBroker[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBrokers();
  }

  public clear(): void {
    this.retrieveAllBrokers();
  }

  public retrieveAllBrokers(): void {
    this.isFetching = true;

    this.brokerService()
      .retrieve()
      .then(
        res => {
          this.brokers = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IBroker): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeBroker(): void {
    this.brokerService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Broker is deleted with identifier ' + this.removeId;
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllBrokers();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
