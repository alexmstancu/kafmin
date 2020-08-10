import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITopic } from '@/shared/model/topic.model';
import TopicService from './topic.service';

@Component
export default class TopicDetails extends Vue {
  @Inject('topicService') private topicService: () => TopicService;
  public topic: ITopic = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.topicId) {
        vm.retrieveTopic(to.params.topicId);
      }
    });
  }

  public retrieveTopic(topicId) {
    this.topicService()
      .find(topicId)
      .then(res => {
        this.topic = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
