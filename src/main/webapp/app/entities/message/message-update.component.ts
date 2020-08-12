import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';
import { IMessage, Message } from '@/shared/model/message.model';
import MessageService from './message.service';

const validations: any = {
  message: {
    key: {},
    message: {},
    partition: {},
  },
};

@Component({
  validations,
})
export default class MessageUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('messageService') private messageService: () => MessageService;
  public message: IMessage = new Message();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.messageId) {
        vm.retrieveMessage(to.params.messageId);
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
    if (this.message.id) {
      this.messageService()
        .update(this.message)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Message is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.messageService()
        .create(this.message)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Message is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveMessage(messageId): void {
    this.messageService()
      .find(messageId)
      .then(res => {
        this.message = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
