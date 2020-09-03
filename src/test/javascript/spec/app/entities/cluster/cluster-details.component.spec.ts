/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ClusterDetailComponent from '@/entities/cluster/cluster-details.vue';
import ClusterClass from '@/entities/cluster/cluster-details.component';
import ClusterService from '@/entities/cluster/cluster.service';
import TopicService from '@/entities/topic/topic.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Cluster Management Detail Component', () => {
    let wrapper: Wrapper<ClusterClass>;
    let comp: ClusterClass;
    let clusterServiceStub: SinonStubbedInstance<ClusterService>;

    beforeEach(() => {
      clusterServiceStub = sinon.createStubInstance<ClusterService>(ClusterService);

      wrapper = shallowMount<ClusterClass>(ClusterDetailComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          clusterService: () => clusterServiceStub,
          topicService: () => new TopicService(),
          alertService: () => new AlertService(store)
        },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // // GIVEN
        // const foundCluster = { id: 123 };
        // clusterServiceStub.find.resolves(foundCluster);

        // // WHEN
        // comp.retrieveCluster(123);
        // await comp.$nextTick();

        // // THEN
        // expect(comp.cluster).toBe(foundCluster);
      });
    });
  });
});
